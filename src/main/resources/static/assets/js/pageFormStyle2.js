  $(document).ready(function() {
    $('#contact_form').bootstrapValidator({
        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
        feedbackIcons: {
            valid: 'glyphicon glyphicon-ok',
            invalid: 'glyphicon glyphicon-remove',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            first_name: {
                validators: {
                        stringLength: {
                        min: 2,
                    },
                        notEmpty: {
                        message: '请输入你的姓名！'
                    }
                }
            },
             last_name: {
                validators: {
                     stringLength: {
                        min: 2,
                    },
                    notEmpty: {
                        message: '请输入你的年龄！'
                    }
                }
            },
            email: {
                validators: {
                    notEmpty: {
                        message: '请输入你的邮箱地址！'
                    },
                    emailAddress: {
                        message: '请输入有效的邮箱地址！'
                    }
                }
            },
            phone: {
                validators: {
                    notEmpty: {
                        message: '请输入你的电话号码！'
                    },
                    phone: {
                        country: 'US',
                        message: '请输入有效的电话号码！'
                    }
                }
            },
            address: {
                validators: {
                     stringLength: {
                        min: 8,
                    },
                    notEmpty: {
                        message: '请输入你的居住街道！'
                    }
                }
            },
            city: {
                validators: {
                     stringLength: {
                        min: 4,
                    },
                    notEmpty: {
                        message: '请输入你所在的城市！'
                    }
                }
            },
            state: {
                validators: {
                    notEmpty: {
                        message: '请输入你所在的国家！'
                    }
                }
            },
            zip: {
                validators: {
                    notEmpty: {
                        message: '请输入你的邮政编码！'
                    },
                    zipCode: {
                        country: 'CH',
                        message: '请输入有效的邮政编码！'
                    }
                }
            },
            comment: {
                validators: {
                      stringLength: {
                        min: 10,
                        max: 200,
                        message:'请输入10~200个文字!'
                    },
                    notEmpty: {
                        message: '请描述你的项目！'
                    }
                    }
                }
            }
        })
        .on('success.form.bv', function(e) {
            $('#success_message').slideDown({ opacity: "show" }, "slow");
            $('#contact_form').data('bootstrapValidator').resetForm();
            e.preventDefault();
            var $form = $(e.target);
            var bv = $form.data('bootstrapValidator');
            $.post($form.attr('action'), $form.serialize(), function(result) {
                console.log(result);
            }, 'json');
        });

      $('#contact_form2').bootstrapValidator({
          feedbackIcons: {
              valid: 'glyphicon glyphicon-ok',
              invalid: 'glyphicon glyphicon-remove',
              validating: 'glyphicon glyphicon-refresh'
          },
          fields: {
              first_name: {
                  validators: {
                      stringLength: {
                          min: 2,
                      },
                      notEmpty: {
                          message: '请输入你的姓名！'
                      }
                  }
              },
              email: {
                  validators: {
                      notEmpty: {
                          message: '请输入你的邮箱地址！'
                      },
                      emailAddress: {
                          message: '请输入有效的邮箱地址！'
                      }
                  }
              },
              companey_name: {
                  validators: {
                      stringLength: {
                          min: 2,
                      },
                      notEmpty: {
                          message: '请输入单位名称！'
                      }
                  }
              },
              companey_phone: {
                  validators: {
                      notEmpty: {
                          message: '请输入单位电话！'
                      },
                      phone: {
                          country: 'CH',
                          message: '请输入有效的单位电话！'
                      }
                  }
              },
              companey_address: {
                  validators: {
                      notEmpty: {
                          message: '请输入单位地址！'
                      }
                  }
              },

              address: {
                  validators: {
                      stringLength: {
                          min: 8,
                      },
                      notEmpty: {
                          message: '请输入你的居住街道！'
                      }
                  }
              },

              state: {
                  validators: {
                      notEmpty: {
                          message: '请输入你所在的国家！'
                      }
                  }
              },
              zip: {
                  validators: {
                      notEmpty: {
                          message: '请输入你的邮政编码！'
                      },
                      zipCode: {
                          country: 'CH',
                          message: '请输入有效的邮政编码！'
                      }
                  }
              },
              website:{
                  validators: {
                      notEmpty: {
                          message: '请输入网站或域名！'
                      }
                  }
              }

          }
      }).on('success.form.bv', function(e) {
              $('#success_message2').slideDown({ opacity: "show" }, "slow");
              $('#contact_form2').data('bootstrapValidator').resetForm();
              e.preventDefault();
              var $form = $(e.target);
              var bv = $form.data('bootstrapValidator');
              $.post($form.attr('action'), $form.serialize(), function(result) {
                  console.log(result);
              }, 'json');
          });
});